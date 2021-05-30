package com.tena.untact2021.controller;

import com.google.common.io.ByteStreams;
import com.tena.untact2021.dto.AttachFile;
import com.tena.untact2021.dto.ResultData;
import com.tena.untact2021.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommonAttachFileController extends BaseController {

	private final AttachFileService fileService;

	@Value("${custom.fileDirPath}")
	private String fileDirPath;

	@GetMapping("/common/file/{relTypeCode}/{relId}/{typeCode}/{type2Code}/{fileNo}")
	public ResponseEntity<Object> showFile(HttpServletRequest request,
	                                         @PathVariable String relTypeCode, @PathVariable int relId,
	                                         @PathVariable String typeCode, @PathVariable String type2Code,
	                                         @PathVariable int fileNo) throws FileNotFoundException {

		AttachFile attachFile = fileService.getFile(relTypeCode, relId, typeCode, type2Code, fileNo);

		if (attachFile == null) {
			// return ResponseEntity.notFound().build();
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			// return new ResponseEntity<>("attachFile not found", HttpStatus.NOT_FOUND);
			// return new ResponseEntity<>(new ResultData("F-1", "attachFile not found."), HttpStatus.NOT_FOUND);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "attachFile not found");
		}

		String filePath = attachFile.getFilePath(fileDirPath);
		Resource resource = new InputStreamResource(new FileInputStream(filePath));

		String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);
	}

	/* Ajax 파일 업로드  */
	@RequestMapping("/common/file/doUpload")
	@ResponseBody
	public ResultData doUpload(MultipartRequest multipartRequest, @RequestParam Map<String, Object> paramMap) {

		return fileService.saveFiles(multipartRequest, paramMap);
	}

	/* 파일 다운로드 */
	@GetMapping("/common/file/doDownload")
	public ResponseEntity<Resource> doDownload(int id, HttpServletRequest request) {
		AttachFile attachFile = fileService.getFileById(id);
		String filePath = attachFile.getFilePath(fileDirPath);

		// 응답 바디에 담을 파일을 리소스로 로드해 담는다
		Resource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			log.info("지정된 경로의 파일을 찾을 수 없습니다. : " + e.getMessage());
		}

		// Content-Type 확인
		String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

		// Content-Type 이 없으면 옥텟 스트림으로 Fallback
		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // application/octet-stream
		}

		// Content-Disposition 헤더 생성
		String contentDisposition = ContentDisposition.attachment()
				.filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8) // 다운로드 파일 이름 초기화
				.build().toString();

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}

	/* 파일 다운로드 V1 : 라이브러리 및 각종 내장 클래스 활용 연습 */
	@RequestMapping(value = "/common/file/doDownloadV1")
	public ResponseEntity<Resource> doDownloadV1(int id, HttpServletRequest request) {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);
		Path path = Path.of(fileRealPath);

		// 응답 바디에 담을 파일을 리소스로 로드해 담는다
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri()); // UrlResource
			// resource = new FileSystemResource(path); // FileSystemResource
		} catch (MalformedURLException e) {
			log.info("Resource Could not be found on the given path.");
		}

		// Content-Type 을 결정한다
		String contentType = null;
		try {
			contentType = Files.probeContentType(path);
			// contentType = request.getServletContext().getMimeType(resource.getFilename());
			// contentType = request.getServletContext().getMimeType(new File(fileRealPath).getAbsolutePath());
			log.info("contentType : " + contentType);
			log.info("canonicalPath : " + resource.getFile().getCanonicalPath());
		} catch (IOException ex) {
			log.info("Could not determine file type.");
		}

		// Content-Type 을 결정할 수 없다면 다음의 기본 타입으로 Fallback
		if(contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			// contentType = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE; // @since 4.0
		}

		// Content-Disposition 헤더 생성
		String contentDisposition = ContentDisposition.attachment()
										.filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8) // 다운로드 파일 이름 초기화
										.build().toString();

		// 응답 헤더와 바디에 담아 리턴
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}


	// 연습 3-2. ResponseEntity 빌더 체이닝 2
	@RequestMapping(value = "/test/responseEntityBuilder2")
	public ResponseEntity<Resource> returnUsingResponseEntityBuilder2(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		Path path = Path.of(fileRealPath);
		Resource resource = new UrlResource(path.toUri());

		// Content-Type
		String contentType = Files.probeContentType(path);

		// Content-Disposition
		ContentDisposition contentDisposition = ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType)); // <=> headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		headers.setContentDisposition(contentDisposition); // <=> headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

		// ResponseEntity 빌더를 사용한 체이닝
		return ResponseEntity.ok()
				.headers(headers) // 맵으로 한꺼번에
				.body(resource);
	}


	// 연습 3-1. ResponseEntity 빌더 체이닝 1
	@RequestMapping(value = "/test/responseEntityBuilder")
	public ResponseEntity<Resource> returnUsingResponseEntityBuilder(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

//		// CASE 1.
//		Path path = Path.of(fileRealPath);
//		Resource resource = new UrlResource(path.toUri());
//		String contentType = Files.probeContentType(path);
//		// String contentType= URLConnection.guessContentTypeFromName(path.toFile().getAbsolutePath());

//		// CASE 2.
//		 File file = new File(fileRealPath);
//		 Resource resource = new UrlResource(file.toURI());
//		 String contentType = Files.probeContentType(file.toPath());
//		 // String contentType= URLConnection.guessContentTypeFromName(file.getAbsolutePath());

//		// CASE 3. 프로토콜 문자열 직접 입력
//		Resource resource = new UrlResource("file", fileRealPath);
//		String contentType = Files.probeContentType(resource.getFile().toPath());
//		// String contentType= URLConnection.guessContentTypeFromName(resource.getFile().getAbsolutePath());

		// CASE 4. ResourceUtils 의 getFile() 또는 getURL()
		Resource resource = new UrlResource(ResourceUtils.getURL(fileRealPath));
		// String contentType= URLConnection.guessContentTypeFromName(resource.getFile().getAbsolutePath());

		String contentType = Files.probeContentType(resource.getFile().toPath());
		String contentDisposition = ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build().toString();

		// ResponseEntity 빌더를 사용한 체이닝
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType)) // <=> .header(HttpHeaders.CONTENT_TYPE, contentType)
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}



	// 연습 2-3.  ResponseBody Type 으로 `FileSystemResource` (Resource) 리턴
	@RequestMapping(value = "/test/resource3")
	public ResponseEntity<Resource> returnFileSystemResourceInResponseBody(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		Path path = Path.of(fileRealPath);

		// Content-Type 추출
		String contentType = Files.probeContentType(path);
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath);

		// 응답 헤더
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDisposition(ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build());

		// 응답 바디 : FileSystemResource
		Resource resource = new FileSystemResource(path);
		// Resource resource = new FileSystemResource(fileRealPath);
		// Resource resource = new FileSystemResource(new File(fileRealPath));

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}


	// 연습 2-2. ResponseBody Type 으로 `UrlResource` (Resource) 리턴
	@RequestMapping(value = "/test/resource2")
	public ResponseEntity<Resource> returnUrlResourceInResponseBody(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		Path path = Path.of(fileRealPath);

		// Content-Type 추출
		String contentType = Files.probeContentType(path);
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath);

		// 응답 헤더
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDisposition(ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build());

		// 응답 바디 : UrlResource
		Resource resource = new UrlResource(path.toUri());
		// Resource resource = new UrlResource(new File(fileRealPath).toURI());

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}



	// 연습 2-1. ResponseBody Type 으로 `InputStreamResource` (Resource) 리턴
	@RequestMapping(value = "/test/resource")
	public ResponseEntity<Resource> returnInputStreamResourceInResponseBody(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		// Path 객체 생성
		Path path = Path.of(fileRealPath);

		// Content-Type 추출
		String contentType = Files.probeContentType(path);
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath);

		// 응답 헤더
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDisposition(ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build());
		// headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		// headers.add(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build().toString());

		// 응답 바디 : InputStreamResource
		Resource resource = new InputStreamResource(Files.newInputStream(path)); // using 'newInputStream()' in Package java.nio.file.Files
		// Resource resource = new InputStreamResource(FileUtils.openInputStream(new File(fileRealPath))); // using 'FileUtils.openInputStream()' in Apache Commons IO

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}



	// 연습 1. ResponseBody Type 으로 `Byte Array` 리턴
	@RequestMapping(value = "/test/byteArray")
	public ResponseEntity<byte[]> returnByteArrayInResponseBody(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		// Path 객체 생성
		Path path = Paths.get(fileRealPath);

		// Content-Type 추출
		String contentType = Files.probeContentType(path);
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath);

		// 응답 헤더
		HttpHeaders headers = new HttpHeaders();

		// 1) Content-Type 헤더 설정
		// headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		headers.setContentType(MediaType.parseMediaType(contentType));

		// 2) Content-Disposition 헤더 설정

		// headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ioFile.getName() + "\"");
		// -> XSS 보안 이슈 : 위와 같이 사용 시, 반드시 filename 의 sanitization 필요
		// -> ContentDisposition 빌더를 사용해 설정

		// 방법 1. add 를 사용한 Content-Disposition 헤더 설정
		// headers.add(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build().toString());
		// headers.add(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build().toString());

		// 방법 2. Content-Disposition 헤더용 setter 를 통한 설정 (훨씬 간편)
		headers.setContentDisposition(ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build());

		// 응답 바디
		byte[] byteArray = ByteStreams.toByteArray(Files.newInputStream(path));// using 'ByteStreams.toByteArray()' in Google Guava
		// byte[] byteArray = IOUtils.toByteArray(Files.newInputStream(path));// using 'IOUtils.toByteArray()' in Apache Commons IO

		return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
	}



	// Load file as `FileSystemResource` and Check Content-Type
	@RequestMapping(value = "/test/downloadFile3")
	public ResponseEntity<Resource> downloadFile3_FileSystemResource(int id, HttpServletRequest request) {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);
		Path path = Paths.get(fileRealPath);

		// Try to Load file as FileSystemResource
		Resource resource = new FileSystemResource(path);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			log.info("contentType : " + contentType);
			log.info("canonicalPath : " + resource.getFile().getCanonicalPath());
		} catch (IOException ex) {
			log.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachFile.getOriginFileName() + "\"")
				.body(resource);
	}



	// Load file as `UrlResource` and Check Content-Type
	@RequestMapping(value = "/test/downloadFile2")
	public ResponseEntity<Resource> downloadFile2_UrlResource(int id, HttpServletRequest request) {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);
		Path path = Paths.get(fileRealPath);

		// Try to Load file as UrlResource
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			log.info("Resource Could not be found on the given path.");
		}

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			log.info("contentType : " + contentType);
			log.info("canonicalPath : " + resource.getFile().getCanonicalPath());
		} catch (IOException ex) {
			log.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachFile.getOriginFileName() + "\"")
				.body(resource);
	}



	// Load file as `InputStreamResource` and Check Content-Type
	@RequestMapping(value = "/test/downloadFile")
	public ResponseEntity<Resource> downloadFile_InputStreamResource(int id, HttpServletRequest request) {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);
		Path path = Paths.get(fileRealPath);

		// Try to Load file as InputStreamResource
		Resource resource = null;
		try {
			resource = new InputStreamResource(Files.newInputStream(path));
		} catch (IOException e) {
			log.info("Resource Could not be loaded through the given InputStream.");
		}

		// Try to determine file's content type
		String contentType = request.getServletContext().getMimeType(path.toFile().getAbsolutePath());
		log.info("contentType : " + contentType);

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachFile.getOriginFileName() + "\"")
				.body(resource);
	}



	// FileCopyUtils.copy(InputStream in, OutputStream out) : 리턴없이 파일의 인풋스트림을 응답객체의 아웃풋스트림으로 복제하는 방식
	@RequestMapping(value = "/test/getFile")
	@ResponseBody
	public void getFile(int id, HttpServletResponse response, HttpServletRequest request) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		// 해당 파일의 인풋 스트림 생성
		InputStream in = new FileInputStream(fileRealPath);

		// Path 객체 생성 : Path.of() 또는 Paths.get() 사용
		Path path = Paths.get(fileRealPath);
		// Path path = Paths.get(fileRealPath).toAbsolutePath().normalize();
		// Path path = Paths.get(new File(fileRealPath).toURI());
		// Path path = Path.of(fileRealPath);
		// Path path = Path.of(fileRealPath).toAbsolutePath().normalize();
		// Path path = Path.of(new File(fileRealPath).toURI());

		// Content-Type 체크
		// 방법 1.
		String contentType = Files.probeContentType(path); // using 'probeContentType()' in Package java.nio.file.Files
		// 방법 2.
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath); // using 'guessContentTypeFromName()' in Package java.net.URLConnection
		// 방법 3
	    // String contentType = request.getServletContext().getMimeType(fileRealPath); // using 'getMimeType()' in Package javax.servlet.ServletContext

		// Content-Type 헤더 설정
		// response.setHeader("Content-Type", contentType);
		response.setContentType(contentType); // Type-safety!

		// Content-Disposition 헤더를 attachment 로 설정하여 다운로드 받을 파일임을 브라우저에게 알려줌
		response.setHeader("Content-Disposition", "attachment; filename=\"" + attachFile.getOriginFileName() + "\""); // -> XSS 보안 이슈: filename sanitization 필요

		FileCopyUtils.copy(in, response.getOutputStream());
	}

}
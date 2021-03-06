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

	/* Ajax ?????? ?????????  */
	@RequestMapping("/common/file/doUpload")
	@ResponseBody
	public ResultData doUpload(MultipartRequest multipartRequest, @RequestParam Map<String, Object> paramMap) {

		return fileService.saveFiles(multipartRequest, paramMap);
	}

	/* ?????? ???????????? */
	@GetMapping("/common/file/doDownload")
	public ResponseEntity<Resource> doDownload(int id, HttpServletRequest request) {
		AttachFile attachFile = fileService.getFileById(id);
		String filePath = attachFile.getFilePath(fileDirPath);

		// ?????? ????????? ?????? ????????? ???????????? ????????? ?????????
		Resource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			log.info("????????? ????????? ????????? ?????? ??? ????????????. : " + e.getMessage());
		}

		// Content-Type ??????
		String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

		// Content-Type ??? ????????? ?????? ??????????????? Fallback
		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // application/octet-stream
		}

		// Content-Disposition ?????? ??????
		String contentDisposition = ContentDisposition.attachment()
				.filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8) // ???????????? ?????? ?????? ?????????
				.build().toString();

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}

	/* ?????? ???????????? V1 : ??????????????? ??? ?????? ?????? ????????? ?????? ?????? */
	@RequestMapping(value = "/common/file/doDownloadV1")
	public ResponseEntity<Resource> doDownloadV1(int id, HttpServletRequest request) {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);
		Path path = Path.of(fileRealPath);

		// ?????? ????????? ?????? ????????? ???????????? ????????? ?????????
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri()); // UrlResource
			// resource = new FileSystemResource(path); // FileSystemResource
		} catch (MalformedURLException e) {
			log.info("Resource Could not be found on the given path.");
		}

		// Content-Type ??? ????????????
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

		// Content-Type ??? ????????? ??? ????????? ????????? ?????? ???????????? Fallback
		if(contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			// contentType = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE; // @since 4.0
		}

		// Content-Disposition ?????? ??????
		String contentDisposition = ContentDisposition.attachment()
										.filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8) // ???????????? ?????? ?????? ?????????
										.build().toString();

		// ?????? ????????? ????????? ?????? ??????
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}


	// ?????? 3-2. ResponseEntity ?????? ????????? 2
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

		// ResponseEntity ????????? ????????? ?????????
		return ResponseEntity.ok()
				.headers(headers) // ????????? ????????????
				.body(resource);
	}


	// ?????? 3-1. ResponseEntity ?????? ????????? 1
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

//		// CASE 3. ???????????? ????????? ?????? ??????
//		Resource resource = new UrlResource("file", fileRealPath);
//		String contentType = Files.probeContentType(resource.getFile().toPath());
//		// String contentType= URLConnection.guessContentTypeFromName(resource.getFile().getAbsolutePath());

		// CASE 4. ResourceUtils ??? getFile() ?????? getURL()
		Resource resource = new UrlResource(ResourceUtils.getURL(fileRealPath));
		// String contentType= URLConnection.guessContentTypeFromName(resource.getFile().getAbsolutePath());

		String contentType = Files.probeContentType(resource.getFile().toPath());
		String contentDisposition = ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build().toString();

		// ResponseEntity ????????? ????????? ?????????
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType)) // <=> .header(HttpHeaders.CONTENT_TYPE, contentType)
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}



	// ?????? 2-3.  ResponseBody Type ?????? `FileSystemResource` (Resource) ??????
	@RequestMapping(value = "/test/resource3")
	public ResponseEntity<Resource> returnFileSystemResourceInResponseBody(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		Path path = Path.of(fileRealPath);

		// Content-Type ??????
		String contentType = Files.probeContentType(path);
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath);

		// ?????? ??????
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDisposition(ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build());

		// ?????? ?????? : FileSystemResource
		Resource resource = new FileSystemResource(path);
		// Resource resource = new FileSystemResource(fileRealPath);
		// Resource resource = new FileSystemResource(new File(fileRealPath));

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}


	// ?????? 2-2. ResponseBody Type ?????? `UrlResource` (Resource) ??????
	@RequestMapping(value = "/test/resource2")
	public ResponseEntity<Resource> returnUrlResourceInResponseBody(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		Path path = Path.of(fileRealPath);

		// Content-Type ??????
		String contentType = Files.probeContentType(path);
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath);

		// ?????? ??????
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDisposition(ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build());

		// ?????? ?????? : UrlResource
		Resource resource = new UrlResource(path.toUri());
		// Resource resource = new UrlResource(new File(fileRealPath).toURI());

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}



	// ?????? 2-1. ResponseBody Type ?????? `InputStreamResource` (Resource) ??????
	@RequestMapping(value = "/test/resource")
	public ResponseEntity<Resource> returnInputStreamResourceInResponseBody(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		// Path ?????? ??????
		Path path = Path.of(fileRealPath);

		// Content-Type ??????
		String contentType = Files.probeContentType(path);
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath);

		// ?????? ??????
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDisposition(ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build());
		// headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		// headers.add(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build().toString());

		// ?????? ?????? : InputStreamResource
		Resource resource = new InputStreamResource(Files.newInputStream(path)); // using 'newInputStream()' in Package java.nio.file.Files
		// Resource resource = new InputStreamResource(FileUtils.openInputStream(new File(fileRealPath))); // using 'FileUtils.openInputStream()' in Apache Commons IO

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}



	// ?????? 1. ResponseBody Type ?????? `Byte Array` ??????
	@RequestMapping(value = "/test/byteArray")
	public ResponseEntity<byte[]> returnByteArrayInResponseBody(int id) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		// Path ?????? ??????
		Path path = Paths.get(fileRealPath);

		// Content-Type ??????
		String contentType = Files.probeContentType(path);
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath);

		// ?????? ??????
		HttpHeaders headers = new HttpHeaders();

		// 1) Content-Type ?????? ??????
		// headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		headers.setContentType(MediaType.parseMediaType(contentType));

		// 2) Content-Disposition ?????? ??????

		// headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ioFile.getName() + "\"");
		// -> XSS ?????? ?????? : ?????? ?????? ?????? ???, ????????? filename ??? sanitization ??????
		// -> ContentDisposition ????????? ????????? ??????

		// ?????? 1. add ??? ????????? Content-Disposition ?????? ??????
		// headers.add(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build().toString());
		// headers.add(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build().toString());

		// ?????? 2. Content-Disposition ????????? setter ??? ?????? ?????? (?????? ??????)
		headers.setContentDisposition(ContentDisposition.attachment().filename(attachFile.getOriginFileName(), StandardCharsets.UTF_8).build());

		// ?????? ??????
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



	// FileCopyUtils.copy(InputStream in, OutputStream out) : ???????????? ????????? ?????????????????? ??????????????? ???????????????????????? ???????????? ??????
	@RequestMapping(value = "/test/getFile")
	@ResponseBody
	public void getFile(int id, HttpServletResponse response, HttpServletRequest request) throws IOException {
		AttachFile attachFile = fileService.getFileById(id);
		String fileRealPath = attachFile.getFilePath(fileDirPath);

		// ?????? ????????? ?????? ????????? ??????
		InputStream in = new FileInputStream(fileRealPath);

		// Path ?????? ?????? : Path.of() ?????? Paths.get() ??????
		Path path = Paths.get(fileRealPath);
		// Path path = Paths.get(fileRealPath).toAbsolutePath().normalize();
		// Path path = Paths.get(new File(fileRealPath).toURI());
		// Path path = Path.of(fileRealPath);
		// Path path = Path.of(fileRealPath).toAbsolutePath().normalize();
		// Path path = Path.of(new File(fileRealPath).toURI());

		// Content-Type ??????
		// ?????? 1.
		String contentType = Files.probeContentType(path); // using 'probeContentType()' in Package java.nio.file.Files
		// ?????? 2.
		// String contentType = URLConnection.guessContentTypeFromName(fileRealPath); // using 'guessContentTypeFromName()' in Package java.net.URLConnection
		// ?????? 3
	    // String contentType = request.getServletContext().getMimeType(fileRealPath); // using 'getMimeType()' in Package javax.servlet.ServletContext

		// Content-Type ?????? ??????
		// response.setHeader("Content-Type", contentType);
		response.setContentType(contentType); // Type-safety!

		// Content-Disposition ????????? attachment ??? ???????????? ???????????? ?????? ???????????? ?????????????????? ?????????
		response.setHeader("Content-Disposition", "attachment; filename=\"" + attachFile.getOriginFileName() + "\""); // -> XSS ?????? ??????: filename sanitization ??????

		FileCopyUtils.copy(in, response.getOutputStream());
	}

}
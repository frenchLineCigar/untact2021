<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>
  (function msgAndView() {
    let historyBack = '${historyBack}' == 'true';
    let msg = '${msg}'.trim();
	  let redirectUri = '${redirectUri}'.trim();

    if (msg) alert(msg);
	  if (historyBack) history.back();
    if (redirectUri) location.replace(redirectUri);
  })();
</script>

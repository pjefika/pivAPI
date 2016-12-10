<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
           prefix="decorator"%>

<div class="container">
    <div class="page-header">
        <h1>Uploader</h1>
    </div>

    <div id="app">
        <div v-if="!image">
            <h2>Select an image</h2>
            <input type="file" @change="onFileChange">
        </div>
        <div v-else>
            <img :src="image" />
            <button @click="removeImage">Remove image</button>
        </div>
        {{image}}
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/vue-components/uploader.js"></script>

require(["sbt/connections/BlogService", "sbt/dom", "sbt/json"],
    function(BlogService, dom, json) {
		var createRow = function(comment) {
		    var table = dom.byId("blogsTable");
		    var tr = document.createElement("tr");
		    table.appendChild(tr);
		    var td = document.createElement("td");
		    td.innerHTML = comment.getTitle();
		    tr.appendChild(td);
		    td = document.createElement("td");
		    td.innerHTML = comment.getCommentUuid();
		    tr.appendChild(td);
		};
	    var blogService = new BlogService(); 
		blogService.getBlogs({ ps: 1 }).then(   //getting first blog by setting page size to 1
	        function(blogs){
	        	return blogs[0].getHandle();
	        }
		).then(
			function(firstBlogHandle){
				return blogService.getBlogComments(firstBlogHandle, { ps: 5 })
	        }
		).then(
	        function(comments){
	            if (comments.length == 0) {
	                text = "All blogs posts returned no results.";
	                dom.setText("content", text);
	            } else {
	                for(var j=0; j<comments.length; j++){
	                    var comment = comments[j];
	                    createRow(comment);
	                }
	            }
	        },
	        function(error){
	            dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
	        }       
		);
	}
);
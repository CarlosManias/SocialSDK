<div id="***REMOVED*** echo $this->config->elementID;?>"></div>

***REMOVED*** 
global $CFG;
require_once $CFG->dirroot . '/blocks/ibmsbt/user_widgets/templates/ibm-sbt-files-grid-row.php';
?>

<script type="text/javascript">
	require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], 
		function(dom, FileGrid) {
			var domNode = dom.byId("fileRow");
			var FileRow = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingHeader");
		    var PagingHeader = domNode.text || domNode.textContent;
		    domNode = dom.byId("pagingFooter");
		    var PagingFooter = domNode.text || domNode.textContent;
		
		    var grid = new FileGrid({
		    	 type : "myFiles",
		    	 endpoint: "***REMOVED*** echo $this->config->endpoint; ?>",
		         hidePager: false,
		         hideSorter: true,
		         
		         hideFooter: false,
		    	 rendererArgs : { template : FileRow, pagerTemplate : PagingHeader, footerTemplate : PagingFooter, pageSize: 2,}       	 
		    });

		    grid.renderer.tableClass = "table";
		    grid.renderer.template = FileRow;
		    
		    dom.byId("***REMOVED*** echo $this->config->elementID;?>").appendChild(grid.domNode);    
		    grid.update();
	});
</script>
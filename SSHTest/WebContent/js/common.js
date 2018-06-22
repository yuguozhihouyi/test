function _change() {
	$("#vCode").attr("src", "/SSHTest/verifyCode/VerifyCodeAction?" + new Date().getTime());
}

/*function _change() {
	var xmlHttp;
	function reload(){
		try{
			xmlHttp = new XMLHttpRequest();
		}catch(e){
			try{
				xmlHttp = new ActiveXObject("Msxml2.XMLHttp");
			}catch(e){
				try{
					xmlHttp=new ActiveXObject("Microsoft.XMLHttp");  
				}catch(e){
					 return false 
				}  
			}
		}	
	}
	var url = "/MyShop/verifyCode/VerifyCodeAction";
	xmlHttp.onreadystatechange = deal;
	xmlHttp.open("GET", url, true);
	xmlHttp.send(null);
}*/

function deal(){
	if(xmlHttp.readyState == 4){
		$("#vCode").attr("src", "/MyShop/verifyCode/VerifyCodeAction?vCode=" + new Date().getTime());
	}
}
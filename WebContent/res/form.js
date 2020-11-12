/**
 * 
 */
function validate() {
	var result = true;
	var errorMessage = "";
	var name = document.getElementById("prefix").value;
	var creds = document.getElementById("minCred").value;

	error=[];
	
	if (creds.trim() == "") {
		error.push("Credit value cannot be empty");
		result = false;
	}
	if (isNaN(creds) || creds < 0) {
		error.push("Credit cannot be negative. Try again!");
		result = false;
	}

	if (name.trim() == "") {
		error.push("Name cannot be left empty.");
		result = false;
	}
	
	if (error.length>0) {
		alert(error.join("\n"))
		result=false;
	}
	
	return result;
}
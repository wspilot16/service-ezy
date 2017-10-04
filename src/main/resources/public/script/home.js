	/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function copyToClipboard(elem) {
	  // create hidden text element, if it doesn't already exist
    var targetId = "_hiddenCopyText_";
    var isInput = elem.tagName === "INPUT";
    var origSelectionStart, origSelectionEnd;
    if (isInput) {
        // can just use the original source element for the selection and copy
        target = elem;
        origSelectionStart = elem.selectionStart;
        origSelectionEnd = elem.selectionEnd;
    } else {
        // must use a temporary form element for the selection and copy
        target = document.getElementById(targetId);
        if (!target) {
            var target = document.createElement("textarea");
            target.style.position = "absolute";
            target.style.left = "-9999px";
            target.style.top = "0";
            target.id = targetId;
            document.body.appendChild(target);
        }
        target.textContent = elem.textContent;
    }
    // select the content
    var currentFocus = document.activeElement;
    target.focus();
    target.setSelectionRange(0, target.value.length);
    
    // copy the selection
    var succeed;
    try {
    	  succeed = document.execCommand("copy");
    } catch(e) {
        succeed = false;
    }
    // restore original focus
    if (currentFocus && typeof currentFocus.focus === "function") {
        currentFocus.focus();
    }
    
    if (isInput) {
        // restore prior selection
        elem.setSelectionRange(origSelectionStart, origSelectionEnd);
    } else {
        // clear temporary content
        target.textContent = "";
    }
    return succeed;
}

function forwardToStep(step){
	switch(step) {
		case 1:
			if($("#step" + step).hasClass("completed")){
				stepActive('step1');
				stepRemove('step2');
				stepRemove('step3');
				stepRemove('step4');
				$('#byte-step1').removeClass('hidden');
				$('#showOptions, #showElements, #finalOutputOuterDiv').addClass('hidden');
			}
			break;
		case 2:
			if($("#step" + step).hasClass("completed")){
				stepActive('step2');				
				stepRemove('step3');
				stepRemove('step4');
				$('#showOptions').removeClass('hidden');
				$('#showElements, #finalOutputOuterDiv').addClass('hidden');
			}
			break;
		case 3:
			if($("#step" + step).hasClass("completed")){
				stepActive('step3');				
				stepRemove('step4');
				$('#showElements').removeClass('hidden');
				$('#finalOutputOuterDiv').addClass('hidden');
			}
			break;
		default:
			break;
	}
}
function onHtmlXmlSuccessResponse(data, isXml, errorCode, errorDescription){
	$('#finalOutput').empty();
	$('#finalOutputOuterDiv').removeClass('hidden');
	$('#showElements').addClass('hidden');
	stepActive('step4');
	stepComplete('step3');
	if(errorCode == null || errorCode == undefined || errorCode == ""){
		$('.hideIfError').removeClass('hidden');
		$('#errorFinalPage').addClass('hidden');
		if(isXml){
			$('#finalOutput').removeClass('hidden');
			$('#finalOutputHtml').addClass('hidden');
			$('#finalOutput').html(data);
		}else{
			$('#finalOutput').addClass('hidden');
			$('#finalOutputHtml').removeClass('hidden');
			var newline = String.fromCharCode(13, 10);
			var htmlCopy = '', html = '';
			$.each(data, function(key, val){						
				htmlCopy += key + " : " + val + newline;
				html+= key.bold() + " : " + val + "<br>";
			});
			$('#finalOutput').html(htmlCopy);
			$('#finalOutputHtml').html(html);
		}
	}else{
		$('.hideIfError, #finalOutputHtml').addClass('hidden');
		$('#errorFinalPage, #finalOutput').removeClass('hidden');
		$('#errorFinalPage').html("Oops we encountered an error - " + errorCode + ". Please go one step back");
		$('#finalOutput').html(errorDescription);
	}
}

function onSuccessElements(keyData, data, methodSelected) {
	$('#dynamicElementForm').empty();
	$('#showElements').removeClass('hidden');
	$('#finalOutputOuterDiv, #showOptions').addClass('hidden');
	stepActive('step3');
	stepComplete('step2');
	stepRemove('step4');
	var html = '';
	selectedMethod = methodSelected;
	$.each(keyData, function (key, val) {        
		html += '<div class="form-group row"><div class="col-sm-3"><label class="pull-left tooltipData" data-toggle="tooltip" data-placement="right" title="'+val.xpathElementName+'" for="' + val.elementName + '">' + val.elementName + '</label></div><div class="col-sm-9"><input data-toggle="tooltip" data-placement="right" title="'+val.xpathElementName+'" type="text" name="'+val.elementName+'" class="form-username form-control tooltipData" id="' + val.elementName + '"></div></div>';
	});
	$('#dynamicElementForm').append(html);
	$('.tooltipData').tooltip();
}

function onOptionsSuccess(data) {
	$('#option_list').empty();
	var html = '';
	stepComplete('step1');
	stepActive('step2');
	$.each(data.webMethodMap, function (key, val) {
		html += "<a href='javascript:void(0)' onclick='onSuccessElements(" + JSON.stringify(val) + "," + JSON.stringify(data) + "," + JSON.stringify(key) + ")' class='list-group-item'><h5 class='list-group-item-heading'>" + key + "</h5></a>";
	});
	$('#option_list').append(html);
	$('#showOptions').removeClass('hidden');
	$('#byte-step1').addClass('hidden');
}

function stepActive(step) {
	$('#' + step).removeClass('completed');
	$('#' + step).addClass('active');
}

function stepComplete(step) {
	$('#' + step).removeClass('active');
	$('#' + step).addClass('completed');
}

function stepRemove(step) {
	$('#' + step).removeClass('active');
	$('#' + step).removeClass('completed');
}

var webMethodUrl = "/webMethodList";
var webResponseUrl = "/webResponse";
var webMethodData = new Object();
var selectedMethod = '';
var xmlResponse = '';
var htmlResponse = '';
var soapHeader = '';
var httpHeader = new Object();
var setHeaderValue = '';
function getHtmlXmlResponse(data){
	$('#overlay').fadeIn();
	$.ajax({
		url: webResponseUrl,
		method: 'POST',
		dataType: 'json',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify(data),
		beforeSend: setHeader,
		success: function(response){
			xmlResponse = response.responseXml;
			htmlResponse = response.responseMap;
			$('#showTimstamp').html(response.responseTime);
			onHtmlXmlSuccessResponse(htmlResponse, false, response.errorCode, response.errorDescription);
		},
		error: function(){
			$('#errorPopUp').modal('show');
			$('#errorPopUp .modal-header h4').text('Something went wrong. Please try again');
		},
		complete: function(){
			$('#overlay').fadeOut();
		}
	});
}
function setHeader(xhr) {
	if(setHeaderValue == 1){
		xhr.setRequestHeader('soapheader', soapHeader);
	}else if(setHeaderValue == 2){
		xhr.setRequestHeader('requestheader', httpHeader);
	}    
}
function callAjax(data){
	$('#overlay').fadeIn();
	$.ajax({
		url: webMethodUrl,
		method: "POST",
		dataType: "json",
		contentType: "application/json",
		data: data,
		success: function(result){	
			webMethodData =  result;
			if(result.errorCode == null){
				onOptionsSuccess(result);
			}else{
				$('#errorPopUp').modal('show');
				$('#errorPopUp .modal-header h4').text('URL entered is not valid or something wrong with the server');
			}
		},
		error: function(err){
			$('#errorPopUp').modal('show');
			$('#errorPopUp .modal-header h4').text('Something went wrong. Please try again');
		},
		complete: function(){
			$('#overlay').fadeOut();
		}
	});

}
function clearFields(){
	$('#soapHeaderText').val('');
	$('#httpKey').val('');
	$('#httpValue').val('');
}
function validURL(str) {
	var regex = /(http|https):\/\/(\w+:{0,1}\w*)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%!\-\/]))?/;
	  if(!regex .test(str)) {
	    return false;
	  } else {
	    return true;
	  }
}
function resetHeaderFields(){
	$("#selectHeaderType").val('0');
	$('#soapHeaderDiv').addClass('hidden');
	$('#httpHeaderDiv').addClass('hidden');
}
$(document).ready(function () {
	$('.tooltipData').tooltip();
	$('#welcomePagePop').modal('show');
	$('#wsdlSubmit').click(function (e) {	
		e.preventDefault();
		$('#showOptions').addClass('hidden');
		var wsdlValue = $('#form-username').val();
		var pattern = wsdlValue;
		stepRemove('step2');
		stepActive('step1');
		stepRemove('step3');
		stepRemove('step4');
		var request = {
				formURL: $('#form-username').val()
		};
		if (wsdlValue === null || wsdlValue === '' || !validURL(wsdlValue)){
			$('#showOptions').addClass('hidden');
			$('#errorPopUp').modal('show');
			$('#errorPopUp .modal-header h4').text('Please enter a valid URL');
			$('#form-username').focus(); 

		} else {
			callAjax(request);
		}
		$('#showElements, #finalOutputOuterDiv').addClass('hidden');
	});
	
	$('#copyButton').click(function(){
		copyToClipboard(document.getElementById("finalOutput"));
	});
	
	$("input[name='type']").click(function(){
		if($('input:radio[name=type]:checked').val() == 'xml'){
			onHtmlXmlSuccessResponse(xmlResponse, true, null, null);
		}else{
			onHtmlXmlSuccessResponse(htmlResponse, false, null, null);
		}
	});
	
	$('#elementsSubmitBtn').click(function(e){
		e.preventDefault();
		webMethodData.selectedMethod = selectedMethod;
		$.each(webMethodData.webMethodMap[selectedMethod],function(key, value){
			value.elementValue = $('input[name='+ value.elementName +']').val();
		});
		if(setHeaderValue == 1){
			webMethodData.soapHeader = soapHeader;
		}else if(setHeaderValue == 2){
			webMethodData.httpRequestHeader = httpHeader;
		}else{
			webMethodData.soapHeader = null;
			webMethodData.httpRequestHeader = null;
		}
		getHtmlXmlResponse(webMethodData);
	});
	$('#overlay').fadeOut();
	
	$("#selectHeaderType").change(function(){
		clearFields();
		setHeaderValue = $(this).val();
		if(setHeaderValue == 1){
			$('#soapHeaderDiv').removeClass('hidden');
			$('#httpHeaderDiv').addClass('hidden');
		}else if(setHeaderValue == 2){
			$('#soapHeaderDiv').addClass('hidden');
			$('#httpHeaderDiv').removeClass('hidden');
		}else{
			resetHeaderFields();
		}
	});
	$('#showHeadersAdded').click(function(){
		soapHeader == ''
		httpHeader == {};
		setHeaderValue = 0;
		webMethodData.soapHeader = null;
		webMethodData.httpRequestHeader = null;
		$(this).addClass('hidden');
		$('#errorPopUp').modal('show');
		$('#errorPopUp .modal-header h4').text('Headers Removed');
	});
	$('#addHeaders').on('shown.bs.modal', function() {
		clearFields();
		resetHeaderFields();
	})
	$('#addHeaderSubmit').click(function(){
		switch(setHeaderValue){
			case "1":
				if($('#soapHeaderText').val() == ''){
					alert("Field cannot be empty");
				}else{
					soapHeader = $('#soapHeaderText').val();
					httpHeader == {};
					$('#showHeadersAdded').removeClass('hidden');
					$('#errorPopUp').modal('show');
					$('#errorPopUp .modal-header h4').text('Soap Header Added');
					$('#addHeaders').modal('hide');
					clearFields();
				}
				break;
			case "2":
				if($('#httpKey').val() == '' || $('#httpValue').val() == ''){
					alert('Key Value Field/s cannot be empty');
				}else{
					httpHeader = {key: $('#httpKey').val() , value: $('#httpValue').val()};
					soapHeader = '';
					$('#errorPopUp').modal('show');
					$('#showHeadersAdded').removeClass('hidden');
					$('#errorPopUp .modal-header h4').text('Soap Header Added');
					$('#addHeaders').modal('hide');
					clearFields();
				}
				break;
				
			default:
				soapHeader == ''
				httpHeader == {};
				$('#showHeadersAdded').addClass('hidden');
				break;
		};
	});
});
$("#soap").click(function(){
	$('.soapView').css('display',"block");
	$('.restView').css('display',"none");
});
$("#rest").click(function(){
	$('.soapView').css('display',"none");
	$('.restView').css('display',"block");
	
});

$("#restSubmit").click(function() {
	var form = $(this).closest("form");
	var serviceData = new Object();
	serviceData.requestUri = $(form).find("#requestUri").val();
	var method = $(form).find("#postType").val();
	$.ajax({		
		url: "http://localhost:8089/rest/"+method,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify(serviceData),
		type: 'post',
		success: function(result, status, xhr) {
			$('.rest-response-view.jjson').jJsonViewer(result.response);
		}
	});
});



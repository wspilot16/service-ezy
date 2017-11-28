import { Component, OnInit, Input, ElementRef, AfterViewInit } from '@angular/core';

declare var $;

@Component({
  selector: 'simple-request-body',
  templateUrl: './simple-request-body.component.html',
  styleUrls: ['./simple-request-body.component.css']
})
export class SimpleRequestBodyComponent {
  @Input() json: Object;
  @Input() requestXml: Object;
  data1: Object = {name: "", values: []};
  htmlToAdd: string;
  constructor(private el: ElementRef) {  }

  ngOnInit() {
  	this.json = {"Header": null,"Body": {"Add": {"intA": "","intB": ""}}};
	let jsonResult = Object.keys(this.json["Body"]);
	let jsonList = {};
	let operation;
	jsonResult.forEach(e => {
		let tempArray = [];
		let tempKeys = Object.keys(this.json["Body"][e]);
		operation = e;
		tempKeys.forEach(key => {
			tempArray.push(this.json["Body"][e][key]);
		});
		jsonList[e] = tempArray;
	});
	this.data1["name"] = operation;
	this.data1["values"] = jsonList[operation];
	let defualt, html = "", submit;
	defualt = '<div><strong style="color: green;">'+ operation +'</strong></div>';
	jsonList[operation].forEach(v => {
		html += "<input id="+v+" type='text' value="+v+">";
	});
	submit="<br><br><button class='btn btn-primary' type='submit' value='Submit'>Submit</button>";
	$('.simpleRequest').append(defualt+html+submit);
	console.log(defualt+html+submit);
	}
}


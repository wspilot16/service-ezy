import { KeyValue } from './../KeyValue';
import { Protocol } from './../protocol.enum';
import { Component, OnInit, Input, ElementRef, AfterViewInit, Output, EventEmitter } from '@angular/core';

declare var $;

@Component({
  selector: 'simple-request-body',
  templateUrl: './simple-request-body.component.html',
  styleUrls: ['./simple-request-body.component.css']
})
export class SimpleRequestBodyComponent implements OnInit {
	readonly MAX_PARSE_DEPTH: number = 4;	
	readonly SKIP_VALUE: string = "SKIP_VALUE";
	
	@Input() requestBody: string;
	@Input() operationName: string;
	@Input() protocol: Protocol;
	prevOperationName: string;
	initialRequestElems: string[] = [];
	filter: string;
	inputs: KeyValue[] = [];
	filteredInputs: KeyValue[] = [];
	parser: DOMParser = new DOMParser();
	@Output() notify: EventEmitter<string> = new EventEmitter<string>();
  constructor(private el: ElementRef) {  }
	
	ngOnInit() {}

	ngOnChanges(): void {
		if (this.requestBody) {
			this.inputs = [];			
			if (Protocol.SOAP == this.protocol) {
				if (this.operationName != this.prevOperationName) {
					this.inputs = [];
					this.filteredInputs = [];
					this.filter = "";
					this.initialRequestElems = [];
				}	
				const root: Document = this.parser.parseFromString(this.requestBody,"text/xml");
				this.parseLeaf(root);
				this.filteredInputs = this.inputs;
			} else if (Protocol.REST == this.protocol) {
				this.parseLeafJson(JSON.parse(this.requestBody));
				this.filteredInputs = this.inputs;
			}
		}
	}

	onModelChange(): void {
		//console.log("on change");
	}

	private addInput(key: string, value: string, fullpath: string, depth): void {
		const kv: KeyValue = new KeyValue();
		kv.key = key;
		kv.value = value;
		kv.fullPath = fullpath;
		kv.depth = depth;
		this.inputs[this.inputs.length] = kv;
	}

	parseLeaf(root: Node, depth?: number, key?, fullpath?: string): void {
		if (!root || depth > 4) {
			return;
		}
		for (var i=0; i<root.childNodes.length; i++) {
			const child = root.childNodes.item(i);
			
			if (child.nodeValue == "?") {
				this.initialRequestElems.push(child.parentNode.nodeName);
				if (!this.inputs.find(item => item.key === key)) {
					this.addInput(key, child.nodeValue, fullpath, depth);
				}
			} else if(this.initialRequestElems.length !=0 && this.initialRequestElems.find(elem => elem === child.parentNode.nodeName)) {
				const found = this.inputs.find(item => item.key === key);
				if (found) {
					found.value = child.nodeValue;
				}
			} else {
				this.parseLeaf(child, depth++, child.localName, fullpath==undefined?'':fullpath+'/'+child.localName);
			}
		}
	}

	updateRequest(root, searchkey, value, depth?: number): void {
		if (!root || depth > this.MAX_PARSE_DEPTH) {
			return;
		}
		for (var i=0; i<root.childNodes.length; i++) {
			if (searchkey == root.childNodes.item(i).localName) {
				root.childNodes.item(i).textContent = value;
			} else {
				this.updateRequest(root.childNodes.item(i), searchkey, value, depth++);
			}
		}
	}

	updateJsonRequest(root, searchkey, value, depth?: number): void {
		if (!root || depth > this.MAX_PARSE_DEPTH) {
			return;
		}

		var that = this;
		var match = false;
	  Object.keys(root).forEach(function(key) {
			if (match) {return;}
			var item = root[key];
			if (typeof item === "string" || typeof item === "number" || item instanceof String) {
				if (searchkey == key) {
					root[key] = value;
					match = true;
					console.log(root);
				}
			} else if (item instanceof Array) {
				that.updateJsonRequest(item, searchkey, value, depth++);
			} else if (!(root instanceof Array) && item instanceof Object) {
				that.updateJsonRequest(item, searchkey, value, depth++);
			}
		});
	}

	public onblur(input: KeyValue): void {
		if (input.value && input.key && input.value!= "?") {
			$('#error-message').text("");
			if (this.protocol == Protocol.REST) {
				const root = JSON.parse(this.requestBody);
				this.updateJsonRequest(root, input.key, input.value);
				console.log(root);
				this.requestBody = JSON.stringify(root);
				this.notify.emit(this.requestBody);
			} else if (this.protocol == Protocol.SOAP) {
				const root: Document = this.parser.parseFromString(this.requestBody,"text/xml");
				this.updateRequest(root, input.key, input.value);
				this.requestBody = new XMLSerializer().serializeToString(root.documentElement);
				this.notify.emit(this.requestBody);
			}
		}
		else{
			if(input.value == "?"){
				$('#error-message').text("Special characters not allowed");
			}else{
				$('#error-message').text("Please enter valid input");
			}
		}
		this.prevOperationName = this.operationName;
	}
	
	parseLeafJson(root: object, depth?: number, key?, fullpath?): void {
		  if (!root || depth > this.MAX_PARSE_DEPTH) {
			  return;
	  }
	  depth = depth?depth:0;
	  
	  var that = this;
	  Object.keys(root).forEach(function(key) {
		var item = root[key];
		if (typeof item === "string" || typeof item === "number" || item instanceof String) {
		  that.addInput(key, item.toString(), fullpath, depth);
		} else if (item instanceof Array) {
		  that.addInput(key, that.SKIP_VALUE, fullpath, depth);
		  that.parseLeafJson(item, depth+1, key, fullpath==undefined?'':fullpath+'/'+key);
		} else if (!(root instanceof Array) && item instanceof Object) {
		  that.addInput(key, that.SKIP_VALUE, fullpath, depth);
		  that.parseLeafJson(item, depth+1, key, fullpath==undefined?'':fullpath+'/'+key);
		} else {
		  that.parseLeafJson(item, depth+1, key, fullpath==undefined?'':fullpath+'/'+key);
		}
  
		if (depth !=0 && that.inputs.length > 0 && Math.abs(that.inputs[that.inputs.length-1].depth - depth) > 0) {
		  that.addInput("---------------------", that.SKIP_VALUE, "", depth+1);
		}
	  });
	}

  public onFilterChange(): void {
		const that = this;
		that.filteredInputs = [];
    this.inputs.forEach(function(input) {
			if (input.key.toLowerCase().indexOf(that.filter.toLowerCase()) != -1) {
				that.filteredInputs.push(input);
			}
		});
  }
}


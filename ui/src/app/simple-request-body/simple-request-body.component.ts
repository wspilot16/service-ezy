import { KeyValue } from './../KeyValue';
import { Component, OnInit, Input, ElementRef, AfterViewInit, Output, EventEmitter } from '@angular/core';

declare var $;

@Component({
  selector: 'simple-request-body',
  templateUrl: './simple-request-body.component.html',
  styleUrls: ['./simple-request-body.component.css']
})
export class SimpleRequestBodyComponent implements OnInit {
	@Input() requestBody: string;
	inputs: KeyValue[] = [];
	parser: DOMParser = new DOMParser();
	@Output() notify: EventEmitter<string> = new EventEmitter<string>();
  constructor(private el: ElementRef) {  }
	
	ngOnInit() {}

	ngOnChanges(): void {
		if (this.requestBody) {
			const root: Document = this.parser.parseFromString(this.requestBody,"text/xml");
			this.parseLeaf(root);
		}
	}

	private addInput(key: string, value: string): void {
		const kv: KeyValue = new KeyValue();
		kv.key = key;
		kv.value = value;
		this.inputs[this.inputs.length] = kv;
	}

	parseLeaf(root: Node, depth?: number, key?): void {
		if (!root || depth > 4) {
			return;
		}
		for (var i=0; i<root.childNodes.length; i++) {
			const child = root.childNodes.item(i);
			if (child.nodeValue == "?") {
				var exists: boolean = false;
				this.inputs.forEach(function(item) {
					if (item.key == key) {
						exists = true;
					}
				});
				if (!exists) {
					this.addInput(key, child.nodeValue);
				}
			} else {
				this.parseLeaf(child, depth++, child.localName);
			}
		}
	}

	updateRequest(root, searchkey, value, depth?: number): void {
		if (!root || depth > 4) {
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

	public onblur(input: KeyValue): void {
		if (input.value && input.key) {
			const root: Document = this.parser.parseFromString(this.requestBody,"text/xml");
			this.updateRequest(root, input.key, input.value);
			this.requestBody = new XMLSerializer().serializeToString(root.documentElement);;
			this.notify.emit(this.requestBody);
		}
	}
}


import { Protocol } from './../protocol.enum';
import { KeyValue } from './../KeyValue';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'simple-response-view',
  templateUrl: './simple-response-view.component.html',
  styleUrls: ['./simple-response-view.component.css']
})
export class SimpleResponseViewComponent implements OnInit {
  @Input() response: string;
  inputs: KeyValue[] = [];
  @Input() requestType: Protocol;
  parser: DOMParser = new DOMParser();
  constructor() { }

  ngOnInit() { }

  ngOnChanges(): void {
		if (this.response) {
      if (this.requestType == Protocol.SOAP) {
        const root: Document = this.parser.parseFromString(this.response,"text/xml");
        this.parseLeafXml(root);
      } else if (this.requestType == Protocol.REST){
        this.parseLeafJson(JSON.parse(this.response));
      }
		}
  }
  
  private addInput(key: string, value: string, depth): void {
    if (value && value != null && value.trim().length > 0) {
      console.log(value);
      const kv: KeyValue = new KeyValue();
      kv.key = key;
      kv.value = value;
      this.inputs[this.inputs.length] = kv;
    }
  }
  
  parseLeafJson(root: object, depth?: number, key?): void {
		if (!root || depth > 4) {
			return;
    }
    
    var that = this;
    Object.keys(root).forEach(function(key) {
      var item = root[key];
      if (typeof item === "string" || item instanceof String) {
        that.addInput(key, item.toString(), depth);
      } else {
        that.parseLeafJson(item, depth++, key);
      }
    });
	}
  
  parseLeafXml(root: Node, depth?: number, key?): void {
		if (!root || depth > 4) {
			return;
    }
    
		for (var i=0; i<root.childNodes.length; i++) {
      const child = root.childNodes.item(i);
      if (child.childNodes && child.childNodes.length > 0) {
        //console.log(child);
        this.parseLeafXml(child, depth++, child.localName);
      } else {
        this.addInput(key, child.nodeValue, depth);
      }
		}
	}

}

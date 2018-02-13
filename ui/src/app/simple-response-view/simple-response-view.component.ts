import { Protocol } from './../protocol.enum';
import { KeyValue } from './../KeyValue';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'simple-response-view',
  templateUrl: './simple-response-view.component.html',
  styleUrls: ['./simple-response-view.component.css']
})
export class SimpleResponseViewComponent implements OnInit {
  readonly MAX_PARSE_DEPTH: number = 10;
  readonly SKIP_VALUE: string = "SKIP_VALUE";
  @Input() response: string;
  filter: string;
  inputs: KeyValue[] = [];
  filteredInputs: KeyValue[] = [];
  @Input() requestType: Protocol;
  parser: DOMParser = new DOMParser();
  constructor() { }

  ngOnInit() { }

  ngOnChanges(): void {
		if (this.response) {
      this.inputs = [];
      if (this.requestType == Protocol.SOAP) {
        const root: Document = this.parser.parseFromString(this.response,"text/xml");
        this.parseLeafXml(root);
        this.filteredInputs = this.inputs;
      } else if (this.requestType == Protocol.REST){
        this.parseLeafJson(JSON.parse(this.response));
        this.filteredInputs = this.inputs;
      }
		}
  }
  
  private addInput(key: string, value: string, depth: number, fullpath: string): void {
    const kv: KeyValue = new KeyValue();
    kv.key = key;
    kv.value = value;
    kv.depth = depth;
    kv.fullPath = fullpath;
    this.inputs[this.inputs.length] = kv;
  }
  
  parseLeafJson(root: object, depth?: number, key?, fullpath?): void {
		if (!root || depth > this.MAX_PARSE_DEPTH) {
			return;
    }
    depth = depth?depth:0;
    
    var that = this;
    Object.keys(root).forEach(function(key) {
      var item = root[key];
      if (typeof item === "string" || typeof item === "number" || typeof item === "boolean" || item instanceof String) {
        that.addInput(key, item.toString(), depth, fullpath);
      } else if (item instanceof Array) {
        that.addInput(key, that.SKIP_VALUE, depth, fullpath);
        that.parseLeafJson(item, depth+1, key, fullpath==undefined?'':fullpath+'/'+key);
      } else if (!(root instanceof Array) && item instanceof Object) {
        that.addInput(key, that.SKIP_VALUE, depth, fullpath);
        that.parseLeafJson(item, depth+1, key, fullpath==undefined?'':fullpath+'/'+key);
      } else {
        that.parseLeafJson(item, depth+1, key, fullpath==undefined?'':fullpath+'/'+key);
      }

      if (depth !=0 && that.inputs.length > 0 && Math.abs(that.inputs[that.inputs.length-1].depth - depth) > 0) {
        that.addInput("---------------------", that.SKIP_VALUE, depth+1, "");
      }
    });
	}
  
  parseLeafXml(root: Node, depth?: number, key?, fullpath?: string): void {
		if (!root || depth > 4) {
			return;
    }
    
		for (var i=0; i<root.childNodes.length; i++) {
      const child = root.childNodes.item(i);
      if (child.childNodes && child.childNodes.length > 0) {
        //console.log(child);
        this.parseLeafXml(child, depth++, child.localName);
      } else {
        this.addInput(key, child.nodeValue, depth, fullpath==undefined?'':fullpath+'/'+key);
      }
		}
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

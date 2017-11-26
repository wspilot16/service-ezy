import { Component, OnInit, ElementRef, ViewChild, AfterViewInit, Input } from '@angular/core';
//import * as $ from 'jquery';
//import * as jJsonViewer from 'jquery.json-viewer/json-viewer';

declare var $;
declare var jJsonViewer: any;

@Component({
  selector: 'app-jjviewer',
  template: '<div></div>',
  styleUrls: ['./jjviewer.component.css']
})
export class JjviewerComponent {
  @Input() json: string;
  @Input() visible: boolean = false;

  constructor(private el: ElementRef) {  }

  ngAfterViewChecked(): void {    
    if (this.json) {
      $(this.el.nativeElement).jJsonViewer(this.json);
    }
  }
}

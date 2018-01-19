import { Protocol } from './../protocol.enum';
import { Component, OnInit, Input } from '@angular/core';
import { KeyValue } from 'app/KeyValue';

@Component({
  selector: 'response-body',
  templateUrl: './response-body.component.html',
  styleUrls: ['./response-body.component.css']
})
export class ResponseBodyComponent {

  @Input() rawResponse: string;
  @Input() response: string;
  @Input() responseHeaders: KeyValue[];
  @Input() requestType: Protocol;
  simpleView: boolean = true;
  @Input() visible: boolean = false;
  activeTabIndex: number = 0;

  toggleResponseView(index: number): void {
    this.simpleView = !this.simpleView;
    this.activeTabIndex = index;
  }
}

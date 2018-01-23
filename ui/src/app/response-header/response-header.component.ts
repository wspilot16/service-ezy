import { Component, OnInit, Input } from '@angular/core';
import { KeyValue } from 'app/KeyValue';

@Component({
  selector: 'app-response-header',
  templateUrl: './response-header.component.html',
  styleUrls: ['./response-header.component.css']
})
export class ResponseHeaderComponent {
  @Input() responseHeaders: KeyValue[];
}

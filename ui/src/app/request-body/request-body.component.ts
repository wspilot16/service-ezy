import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Protocol } from './../protocol.enum';

@Component({
  selector: 'request-body',
  templateUrl: './request-body.component.html',
  styleUrls: ['./request-body.component.css']
})
export class RequestBodyComponent implements OnInit {
  @Input() requestBody: string;
  @Input() operationName: string;
  simpleView: boolean = true;
  @Input() visible: boolean;
  @Output() notify: EventEmitter<string> = new EventEmitter<string>();
  @Input() htmlToAdd: string;
  @Input() requestType: Protocol;
  constructor() { }

  ngOnInit() { }

  public onChange(): void {
    this.notify.emit(this.requestBody);
  }

  toggleRequestView(): void {
    this.simpleView = !this.simpleView;
  }

  public onNotify(requestBody: string): void {
    this.requestBody = requestBody;
    this.onChange();
  }
}

import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'request-body',
  templateUrl: './request-body.component.html',
  styleUrls: ['./request-body.component.css']
})
export class RequestBodyComponent implements OnInit {
  @Input() requestBody: string;
  simpleView: boolean = false;
  @Input() visible: boolean = true;
  @Output() notify: EventEmitter<string> = new EventEmitter<string>();

  constructor() { }

  ngOnInit() {
    console.log(this.requestBody);
    console.log(this.visible);
  }

  public onChange(): void {
    console.log("changed "+this.requestBody);
    this.notify.emit(this.requestBody);
  }

  toggleRequestView(): void {
    this.simpleView = !this.simpleView;
  }
}

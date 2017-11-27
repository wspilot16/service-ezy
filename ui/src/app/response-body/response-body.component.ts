import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'response-body',
  templateUrl: './response-body.component.html',
  styleUrls: ['./response-body.component.css']
})
export class ResponseBodyComponent {

  @Input() rawResponse: string;
  @Input() response: string;
  simpleView: boolean = true;
  @Input() visible: boolean = false;

  toggleResponseView(): void {
    this.simpleView = !this.simpleView;
  }
}

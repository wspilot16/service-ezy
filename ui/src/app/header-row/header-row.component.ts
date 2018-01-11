import { KeyValue } from './../KeyValue';
import { Component, OnInit, Input } from '@angular/core';
import { Event } from '_debugger';

@Component({
  selector: 'app-header-row',
  templateUrl: './header-row.component.html',
  styleUrls: ['./header-row.component.css']
})
export class HeaderRowComponent {
  @Input() headers: KeyValue[];

  onKey(event: any): void {
    if (event.target && (event.target.value as string).length > 0 && this.shouldAddRow())
      this.headers.push(new KeyValue());
  }

  private shouldAddRow(): boolean {debugger;
    const lastRow: KeyValue = this.headers[this.headers.length - 1];
    return lastRow.isEmpty();
  }
}

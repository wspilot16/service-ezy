import { Component, OnInit, Pipe, PipeTransform, Input } from '@angular/core';
import * as vkbeautify from 'vkbeautify';

@Component({
  selector: 'app-xmlformatter',
  template: '<div></div>',
  styleUrls: ['./xmlformatter.component.css']
})

@Pipe({
  name: 'xml'
})

export class XmlformatterComponent implements PipeTransform {

  transform(value: string): string {
      return vkbeautify.xml(value);
  }
}


import { Protocol } from './../protocol.enum';
import { RequestType } from './../request-type.enum';
import { ServiceData } from './../service-data';
import { ClientService } from './../client.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'tab',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class TabComponent implements OnInit {
  constructor(private clientService?: ClientService) { }
  title: string;
  id: number;
  data: ServiceData;
  active: boolean = false;
  requestBodyVisible: boolean = false;
  restProtocolActive: boolean = true;
  requestTypes: RequestType[];

  ngOnInit() {
    this.data = new ServiceData();
    this.data.requestUri = "https://reqres.in/api/users";
    this.data.requestType = RequestType.GET;
    this.requestTypes = [RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE, RequestType.HEAD];
  }

  public clicked(): void {console.log(this.data.protocol);
    this.clientService.getResponse(this.data).then(responseData=>{this.data = responseData; console.log(responseData);});
  }

  public getTitle(): string {
    return this.title;
  }

  public getId(): number {
    return this.id;
  }

  public requestTypeClicked(type): void {
    this.requestBodyVisible = (RequestType.PUT == type || RequestType.POST == type);
  }

  public protocolClick(): void {
    this.restProtocolActive = !this.restProtocolActive;
    this.data.protocol = this.restProtocolActive?Protocol.REST:Protocol.SOAP;
  }

}

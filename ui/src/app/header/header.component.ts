import { SoapOperation } from './../soap-operation';
import { Protocol } from './../protocol.enum';
import { RequestType } from './../request-type.enum';
import { ServiceData } from './../service-data';
import { ClientService } from './../client.service';
import { Component, OnInit, ViewChild } from '@angular/core';

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
  selectedOperationName: string;
  soapOperations: SoapOperation[];

  ngOnInit() {
    this.data = new ServiceData();
    this.data.requestUri = "http://www.dneonline.com/calculator.asmx?WSDL";
    this.data.requestType = RequestType.GET;
    this.data.protocol = Protocol.REST;
    this.data.soapOperation = new SoapOperation();
    this.requestTypes = [RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE, RequestType.HEAD];
  }

  public goClicked(): void {
    if (this.data.protocol == Protocol.SOAP) {

    }
    this.clientService.getResponse(this.data).then(responseData=>{this.data = responseData; 
      if (this.data.protocol == Protocol.SOAP) {
        this.data.soapOperation = this.data.soapOperations[0];
        this.soapOperations = this.data.soapOperations;
        this.data.soapOperations = null;
        if (this.data.soapOperation) {
          this.data.requestBody = this.data.soapOperation.requestTemplate;
        }
      }
      console.log(responseData.soapOperation);});
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

  public requestTypeChanged(value: any): void {
    console.log("changed"+value);
  }

  public protocolClick(): void {
    this.restProtocolActive = !this.restProtocolActive;
    this.data.protocol = this.restProtocolActive?Protocol.REST:Protocol.SOAP;
    
    if (this.restProtocolActive) {
      this.data.requestType = RequestType.GET;
      this.requestTypes = [RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE, RequestType.HEAD];
    } else {
      this.data.requestType = RequestType.POST;
      this.requestTypes = [RequestType.POST];
    }
  }

  public operationClick(operationName: string): void {
    this.soapOperations.forEach(soapOperation => {
      if (soapOperation.name == operationName) {
        this.data.requestBody = soapOperation.requestTemplate;
        this.data.soapOperation = soapOperation;
      }
    });
  }

  public onRequestUriChange(): void {
    if (this.data.protocol == Protocol.SOAP && this.data.requestUri) {
      console.log(this.data.requestUri);
      this.goClicked();
    }
  }

}

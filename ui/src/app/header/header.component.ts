import { KeyValue } from './../KeyValue';
import { SoapOperation } from './../soap-operation';
import { Protocol } from './../protocol.enum';
import { RequestType } from './../request-type.enum';
import { ServiceData } from './../service-data';
import { ClientService } from './../client.service';
import { Component, OnInit, ViewChild } from '@angular/core';

declare var $;

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
  simpleView: boolean = true;
  requestTypes: RequestType[];
  protocolName: string = Protocol.REST.toString();
  operationName: string;
  soapOperations: SoapOperation[] = [];
  requestBody: string;
  showHeaderRows: boolean = false;

  ngOnInit() {
    this.data = new ServiceData();
    this.data.headers = [new KeyValue()];
    //this.data.requestUri = "http://www.dneonline.com/calculator.asmx?WSDL";
    this.data.requestUri = "https://reqres.in/api/users?page=2";
    this.data.requestType = RequestType.GET;
    this.data.protocol = Protocol.REST;
    this.data.soapOperation = new SoapOperation();
    this.requestTypes = [RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE, RequestType.HEAD];
  }

  public goClicked(): void {
    var uri = $('#requestUri').val();
    var pattern = /^(http|https):\/\/[^ "]+$/;
    var valid = pattern.test(uri);
    if($('#requestInput').length){
      var val = $('#requestInput').val();
      if(val == "?" || val == ""){
        $('#error-message').text("Please enter valid input");
      }else{
        $('#error-message').text("");
      }
    }
    if(!valid){
      $('#uri-error').css('display',"block");
    } else {
      $('#uri-error').css('display',"none");
      $('#overlay').fadeIn();
      this.data.soapOperation.requestTemplate = this.requestBody;
      this.data.requestBody = this.requestBody;
      const validHeaders: KeyValue[] = [];
      this.data.headers.forEach(kv => {if (kv.isEmpty()) { validHeaders.push(kv) } });
      this.data.headers = validHeaders;
      this.clientService.getResponse(this.data).then(responseData=>{this.data = responseData;
        if (this.data.protocol == Protocol.SOAP) {
          if (this.data.soapOperations && this.data.soapOperations.length > 0) {
            this.data.soapOperation = this.data.soapOperations[0];
            this.soapOperations = this.data.soapOperations;
            this.data.soapOperations = null;
          }
          if (this.data.soapOperation && !this.requestBody) {
            this.requestBody = this.data.soapOperation.requestTemplate;
          }
        }
        $('#overlay').fadeOut();
        if($('#response-label').length){
          window.setTimeout(function(){ $('#response-label')[0].scrollIntoView(!0);},500);
        }
      });
    }
  }
  enableGo(event:any) {
    var uri = $('#requestUri').val();
    var pattern = /^(http|https):\/\/[^ "]+$/;
    var valid = pattern.test(uri);
    if(valid){
      $('#goButton').removeAttr('disabled');
    }
    else{
      $('#uri-error').removeAttr('hidden');
    }
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
    this.data.response = "";
    this.data.rawResponse = "";
    this.data.requestUri = "";
    this.protocolName = this.data.protocol.toString();
  }

  public operationClick(operationName: string): void {
    this.operationName = operationName;
    this.soapOperations.forEach(soapOperation => {
      if (soapOperation.name == operationName) {
        this.requestBody = soapOperation.requestTemplate;
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

  public toggleRequestView(): void {
    this.simpleView = !this.simpleView;
  }

  public onNotify(requestBody: string): void {
    this.requestBody = requestBody;
  }

}

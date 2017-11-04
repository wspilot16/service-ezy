import { RequestType } from './../request-type.enum';
import { ServiceData } from './../service-data';
import { ClientService } from './../client.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  constructor(private clientService: ClientService) { }
  data: ServiceData;
  requestTypes: RequestType[];

  ngOnInit() {
    this.data = new ServiceData();
    this.data.requestUri = "https://reqres.in/api/users";
    this.data.requestType = RequestType.GET;
    this.requestTypes = [RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE, RequestType.HEAD];
  }

  public clicked():void {
    console.log("clicked"+this.data.requestUri);
    this.clientService.getResponse(this.data).then(responseData=>{this.data = responseData; console.log(responseData);});
  }

}

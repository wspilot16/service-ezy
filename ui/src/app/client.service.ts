import { ServiceData } from './service-data';
import { Http, RequestOptions } from '@angular/http';
import { Injectable } from '@angular/core';
import 'rxjs/Rx';

@Injectable()
export class ClientService {
  url = "http://localhost:8089/rest/post";
  constructor(private http: Http) { }

  getResponse(data: ServiceData):Promise<ServiceData> {
    console.log(data);
    return this.http.post(this.url, data).map(response => response.json() as ServiceData)
    .toPromise()
    .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

}

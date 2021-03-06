import { ClientService } from './client.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Http, HttpModule } from '@angular/http';
import { Router, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { TabComponent } from './header/header.component';
import { RequestBodyComponent } from './request-body/request-body.component';
import { ResponseBodyComponent } from './response-body/response-body.component';
import { JjviewerComponent } from './jjviewer/jjviewer.component';
import { SimpleRequestBodyComponent } from './simple-request-body/simple-request-body.component';
import { SimpleResponseViewComponent } from './simple-response-view/simple-response-view.component';
import { HeaderRowComponent } from './header-row/header-row.component';

@NgModule({
  declarations: [
    AppComponent,
    TabComponent,
    RequestBodyComponent,
    ResponseBodyComponent,
    JjviewerComponent,
    SimpleRequestBodyComponent,
    SimpleResponseViewComponent,
    HeaderRowComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule  
  ],
  providers: [ClientService],
  bootstrap: [AppComponent]
})
export class AppModule { }

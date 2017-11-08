import { ClientService } from './client.service';
import { TabComponent } from './header/header.component';
import { Component, OnInit, ComponentFactoryResolver, ViewChild, ViewContainerRef } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{ 
  title = 'Service Ezy';
  tabs: TabComponent[] = [];
  tabCtr: number = 1;
  constructor(private clientService: ClientService) {}

  public addOnclick(): void {
    this.addTab();
  }

  public addTab(): void {
    var tabId = this.tabCtr++;
    const tabComponent = new TabComponent(this.clientService);
    tabComponent.title = "Tab " + tabId;
    tabComponent.id = tabId;
    tabComponent.active = true;
    this.tabs.forEach(tab => {
      tab.active = false;
    })
    this.tabs.push(tabComponent);
    this.tabClick(tabComponent);
  }

  public ngOnInit(): void {
    this.addTab();
  }

  public tabClick(tab: TabComponent): void {
    this.tabs.forEach(tab => {
      tab.active = false;
    });

    tab.active = true;
  }

  public tabClose(selectedTab: TabComponent): void {
    const index: number = this.tabs.indexOf(selectedTab);
    this.tabs.splice(index, 1);
    const nextActiveIndex = index == 0 ? 0 : index-1;
    this.tabs.forEach(tab => {
      tab.active = false;
    });
    this.tabs[nextActiveIndex].active = true;
  }
}

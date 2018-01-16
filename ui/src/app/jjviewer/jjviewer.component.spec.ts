import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JjviewerComponent } from './jjviewer.component';

describe('JjviewerComponent', () => {
  let component: JjviewerComponent;
  let fixture: ComponentFixture<JjviewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JjviewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JjviewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

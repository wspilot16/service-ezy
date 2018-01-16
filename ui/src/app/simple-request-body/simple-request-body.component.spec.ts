import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SimpleRequestBodyComponent } from './simple-request-body.component';

describe('SimpleRequestBodyComponent', () => {
  let component: SimpleRequestBodyComponent;
  let fixture: ComponentFixture<SimpleRequestBodyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SimpleRequestBodyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimpleRequestBodyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

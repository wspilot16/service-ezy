import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SimpleResponseViewComponent } from './simple-response-view.component';

describe('SimpleResponseViewComponent', () => {
  let component: SimpleResponseViewComponent;
  let fixture: ComponentFixture<SimpleResponseViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SimpleResponseViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimpleResponseViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

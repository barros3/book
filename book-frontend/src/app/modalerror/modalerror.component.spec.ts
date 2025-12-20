import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalerrorComponent } from './modalerror.component';

describe('ModalerrorComponent', () => {
  let component: ModalerrorComponent;
  let fixture: ComponentFixture<ModalerrorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ModalerrorComponent]
    });
    fixture = TestBed.createComponent(ModalerrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

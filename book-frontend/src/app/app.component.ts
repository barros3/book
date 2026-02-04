import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  //styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Stackflow';
  isNavbarCollapsed = true;

  constructor() {}

  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    
    // Verifica se o clique NÃO foi dentro de um menu específico
    if (!target.closest(".orgobom-menu")) {
      // Fecha todos os menus
      document.querySelectorAll(".orgobom-menu").forEach(menu => {
        menu.classList.remove("show");
      });
    }
  }

  toggleNavbar() {
    this.isNavbarCollapsed =!this.isNavbarCollapsed;
  }

}
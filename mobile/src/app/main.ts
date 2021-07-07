import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app.module';

// for production only
// import { enableProdMode } from "@angular/core";
// enableProdMode();

platformBrowserDynamic().bootstrapModule(AppModule);

import { NgModule } from '@angular/core';
import { SafeHtmlPipe } from './safeHtml/safeHtml';
import { SecondsToTimePipe } from './secondsToTime/secondsToTime';

@NgModule({
    declarations: [SafeHtmlPipe, SecondsToTimePipe],
    imports: [],
    exports: [SafeHtmlPipe, SecondsToTimePipe]
})
export class PipesModule {
}

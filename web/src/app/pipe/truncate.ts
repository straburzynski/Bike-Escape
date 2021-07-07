import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {
  transform(textValue: string, textLength: number = 100): string {
    return textValue.length > textLength ? textValue.substring(0, textLength) + ' ...' : textValue;
  }
}

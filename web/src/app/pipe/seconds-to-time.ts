import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'secondsToTime'
})
export class SecondsToTimePipe implements PipeTransform {
  transform(seconds: number): string {
    function z(n) {
      return (n < 10 ? '0' : '') + n;
    }
    const s = seconds % 60;
    const m = Math.floor(seconds % 3600 / 60);
    const h = Math.floor(seconds / 3600);
    return (z(h) + ':' + z(m) + ':' + z(s));
    }
}

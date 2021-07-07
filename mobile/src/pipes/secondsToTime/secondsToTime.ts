import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'secondsToTime'
})
export class SecondsToTimePipe implements PipeTransform {
    transform(inputSeconds: number): string {
        const secNum = parseInt(inputSeconds.toString(), 10);
        const hours = Math.floor(secNum / 3600);
        const minutes = Math.floor((secNum - (hours * 3600)) / 60);
        const seconds = secNum - (hours * 3600) - (minutes * 60);
        const hoursString = (hours < 10) ? '0' + hours : hours.toString();
        const minutesString = (minutes < 10) ? '0' + minutes : minutes.toString();
        const secondsString = (seconds < 10) ? '0' + seconds : seconds.toString();
        return hoursString + ':' + minutesString + ':' + secondsString;
    }
}

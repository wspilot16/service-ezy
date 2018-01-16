export class KeyValue {
    key: string;
    value: string;
    depth: number;
    fullPath: string;

    isEmpty(): boolean {
      if (this.key || this.value) {
        return true;
      }
      return false;
    }
}

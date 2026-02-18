export interface FilterField {
  key: string;
  label: string;
  type: 'text' | 'select';
  options?: { value: any; label: string }[];
  placeholder?: string;
}

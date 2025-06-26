export interface ProfileResult {
  columnName: string;
  dataType: string;
  completeness: number;
  distinctCount: number;
  mean?: number;
  stdDev?: number;
  min?: string;
  max?: string;
  nullCount: number;
  totalCount: number;
}
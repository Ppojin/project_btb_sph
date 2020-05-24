import { Category } from 'app/shared/model/enumerations/category.model';

export interface IQABank {
  id?: number;
  idQABank?: string;
  title?: string;
  contents?: string;
  gitUrl?: string;
  category?: Category;
}

export const defaultValue: Readonly<IQABank> = {};

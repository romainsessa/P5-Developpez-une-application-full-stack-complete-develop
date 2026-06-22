import { Topic } from "./topic.interface";

export interface User {
	id: number,
	username: string,
	email: string,
	created_at: Date,
	updated_at: Date
	topics?: Topic[];
}

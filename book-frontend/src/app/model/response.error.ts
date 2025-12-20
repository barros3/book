
export class ResponseError {
	statusCode!: string;
	errorMessage!: string;
	tille!: string;
	constructor(
		public code: string,
		public message: string,
		public title: string
	){
		this.statusCode = code;
		this.errorMessage = message;
		this.title = title;
	}
	
}

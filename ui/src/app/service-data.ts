import { KeyValue } from './KeyValue';
import { SoapOperation } from './soap-operation';
import { Protocol } from './protocol.enum';
import { RequestType } from './request-type.enum';
export class ServiceData {
    wsdlUrl: string;
	private responseTime: number;
	private errorDescription: string;
	private errorCode: number;
	response: string;
	rawResponse: string;
	requestUri: string;
	requestType: RequestType;
	requestBody: string;
	headers: KeyValue[];
	protocol: Protocol;
	soapOperation: SoapOperation;
	soapOperations: SoapOperation[];
}

import { SoapOperation } from './soap-operation';
import { Protocol } from './protocol.enum';
import { RequestType } from './request-type.enum';
export class ServiceData {
    wsdlUrl: string;
	responseXml: string;
	private responseJson: string;
	private responseTime: number;
	private errorDescription: string;
	private errorCode: number;
	response: string;
	requestUri: string;
	requestType: RequestType;
	requestBody: string;
	protocol: Protocol;
	soapOperation: SoapOperation;
	soapOperations: SoapOperation[];
}

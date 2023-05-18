// axios 요청 오류, axios 요청의 구성 옵션, axios 요청의 응답
import { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';

export type RequestType = (config: AxiosRequestConfig) => Promise<AxiosResponse>;

export type Method = 'get' | 'post' | 'put' | 'patch' | 'delete';

export type RequestOptions<T> = {
    method: Method;
    url: string;
    data?: T;
    config?: AxiosRequestConfig;
    onSuccess?: (response: AxiosResponse<T>) => void;
    onError?: (error: AxiosError) => void;
};

export type RequestParams = [url: string, config?: AxiosRequestConfig];
export type RequestParamsWithData = [url: string, data?: any, config?: AxiosRequestConfig];
export type RequestArguments = RequestParams | RequestParamsWithData;
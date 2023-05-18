import axios, { AxiosInstance } from 'axios';
import {RequestType} from "@/api/module/api.type";

const axiosInstance: RequestType & AxiosInstance = axios.create({
    baseURL: 'http://localhost:8081/api',
    timeout: 3000,
});

axiosInstance.interceptors.request.use(
    (config) => config,
    (error) => Promise.reject(error),
);

axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        const customError = handleErrorResponse(error);
        return Promise.reject(customError);
    },
);

export class CustomError extends Error {
    originError: Error;
    code?: number;
    validation?: any;

    constructor(name: string | null, message: string | undefined, originError: Error) {
        super(message);
        this.name = name || 'CustomError';
        this.originError = originError;
    }
}

export const handleErrorResponse = (error: CustomError | Error) => {
    // const errorMessage = error?.message || 'Unknown error';
    // const customError = new CustomError(null, errorMessage, error);
    const errorMessage = error?.message || 'Unknown error';
    const errorCode = (error as any)?.code;
    const errorValidation = (error as any)?.validation;

    const customError = new CustomError(null, errorMessage, error);
    customError.code = errorCode;
    customError.validation = errorValidation;

    if (error instanceof CustomError) {
        console.error('CustomError:', customError.name, customError.message, customError.code, customError.validation);
    } else {
        console.error('Error:', customError.originError?.name, customError.message, customError.code, customError.validation);
    }

    return customError;
};

export default axiosInstance;
import axiosInstance from "@/api/module/axiosInstance";
import { Method, RequestArguments, RequestOptions } from '@/api/module/api.type';

const requestWithOptions = <T>(options: RequestOptions<T>) =>
    axiosInstance[options.method]<T>(options.url, options.data, options.config)
        .then((response) => {
            if (options.onSuccess) {
                options.onSuccess(response);
            }
            return response.data;
        })
        .catch((error) => {
            if (options.onError) {
                options.onError(error);
            }
            throw error;
        });

const request =
    (method: Method) =>
        <T>(...args: RequestArguments) => {
            if (['get', 'delete'].includes(method)) {
                const [url, config] = args;
                return requestWithOptions<T>({ method, url, config });
            }
            if (['post', 'put', 'patch'].includes(method)) {
                const [url, data, config] = args;
                return requestWithOptions<T>({ method, url, data, config });
            }
            throw new Error(`Method 값이 올바르지 않습니다: ${method}`);
        };

export default {
    get: request('get'),
    post: request('post'),
    put: request('put'),
    patch: request('patch'),
    delete: request('delete'),
};
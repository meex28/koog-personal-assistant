import {useState} from "react";

export default function useFetch<T, G = any>(url: string, method: 'GET' | 'POST' = 'GET') {
    const [data, setData] = useState<T | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<Error | null>(null);

    const execute = async (body?: G) => {
        setLoading(true);
        setError(null);

        try {
            const res = await fetch(url, {
                method,
                headers: {"Content-Type": "application/json"},
                body: body ? JSON.stringify(body) : undefined
            });

            if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

            const result: T = await res.json();

            setData(result);
            setLoading(false);

            return result;
        } catch (err: any) {
            console.error(err)
            setError(err);
            setLoading(false);
        }
    };

    return {data, execute, loading, error};
}

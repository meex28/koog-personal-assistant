import useFetch from "@/hooks/useFetch";

interface ChatRequest {
    message: string;
}

interface ChatResponse {
    message: string;
}

export default function usePostChatMessage() {
    return useFetch<ChatResponse, ChatRequest>(
        "http://localhost:8080/ai/chat",
        'POST'
    );
}
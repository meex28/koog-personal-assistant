import useFetch from "@/hooks/useFetch";

export interface ChatHistory {
    messages: ChatMessage[];
}

export interface ChatMessage {
    from: "user" | "assistant" | "system";
    content: string;
}

export default function usePostChatMessage() {
    return useFetch<ChatHistory, ChatHistory>(
        "http://localhost:8080/ai/chat",
        'POST'
    );
}
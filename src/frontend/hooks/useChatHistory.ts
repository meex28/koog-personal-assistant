import {useState, useCallback} from "react";
import {ChatMessage} from "@/hooks/useApi";

export default function useChatHistory(initialMessages: ChatMessage[] = []) {
    const [messages, setMessages] = useState<ChatMessage[]>(initialMessages);

    const addMessage = useCallback((message: ChatMessage): Promise<ChatMessage[]> => {
        return new Promise((resolve) => {
            setMessages(prev => {
                const updatedMessages = [...prev, message];
                resolve(updatedMessages);
                return updatedMessages;
            });
        });
    }, []);

    const addUserMessage = useCallback((content: string): Promise<ChatMessage[]> => {
        return addMessage({ from: "user", content });
    }, [addMessage]);

    const addAssistantMessage = useCallback((content: string): Promise<ChatMessage[]> => {
        return addMessage({ from: "assistant", content });
    }, [addMessage]);

    const replaceChatHistory = useCallback((newMessages: ChatMessage[]) => {
        setMessages(newMessages);
    }, []);

    return {
        messages,
        addMessage,
        addUserMessage,
        addAssistantMessage,
        replaceChatHistory
    };
}
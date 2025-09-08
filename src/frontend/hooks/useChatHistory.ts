import {useState} from "react";
import type {UIMessage} from "ai";

type ChatMessage = { from: UIMessage['role'], content: string };

export default function useChatHistory(initialMessages: ChatMessage[] = []) {
    const [messages, setMessages] = useState<ChatMessage[]>(initialMessages);

    const addMessage = (message: ChatMessage) => {
        setMessages(prev => [...prev, message]);
    };

    const addUserMessage = (content: string) => {
        addMessage({ from: "user", content });
    };

    const addAssistantMessage = (content: string) => {
        addMessage({ from: "assistant", content });
    };

    return {
        messages,
        addMessage,
        addUserMessage,
        addAssistantMessage
    };
}
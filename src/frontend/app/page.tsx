'use client'

import React from "react";
import ChatHistory from "@/components/ChatHistory";
import MessageInput from "@/components/MessageInput";
import usePostChatMessage from "@/hooks/useApi";
import useChatHistory from "@/hooks/useChatHistory";

export default function Home() {
    const {messages, addUserMessage, addAssistantMessage} = useChatHistory([
        {from: "assistant", content: "Hello! How can I help you today?"}
    ]);
    const {execute: sendMessage, loading, error} = usePostChatMessage();

    const submitUserMessage = async (message: string) => {
        addUserMessage(message);
        const response = await sendMessage({message});
        if (response) {
            addAssistantMessage(response.message)
        } else {
            console.error(`Failed to get response from the server. Response: ${response}`);
        }
    };

    return (
        <div className="max-w-4/5 h-full mx-auto flex flex-col justify-end py-10">
            <ChatHistory messages={messages}/>
            <MessageInput onSubmit={submitUserMessage} isLoading={loading}/>
            {error && <div className="text-red-500 mt-2">Error: {error.message}</div>}
        </div>
    );
}

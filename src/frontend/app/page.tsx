'use client'

import {Conversation, ConversationContent, ConversationScrollButton} from "@/components/ai-elements/conversation";
import {Message, MessageContent} from "@/components/ai-elements/message";
import type {UIMessage} from "ai";
import {
    PromptInput,
    PromptInputSubmit,
    PromptInputTextarea,
    PromptInputToolbar
} from "@/components/ai-elements/prompt-input";
import React, {FormEvent, useState} from "react";

export default function Home() {
    const [messages, setMessages] = useState<{ from: UIMessage['role'], content: string }[]>([
        {from: "user", content: "Hi there!"},
        {from: "assistant", content: "Hello! How can I help you today?"}
    ]);
    const [prompt, setPrompt] = useState("")
    const sendMessage = (e: FormEvent) => {
        e.preventDefault();

        // Don't send empty messages
        if (!prompt.trim()) return;

        setMessages(prevMessages => [
            ...prevMessages,
            {from: "user", content: prompt}
        ]);
        setPrompt("");
    };

    return (<div className="max-w-4/5 m-auto">
            <Conversation>
                <ConversationContent>
                    {messages.map((message) => (
                        <Message from={message.from} key={message.content}>
                            <MessageContent>{message.content}</MessageContent>
                        </Message>
                    ))}
                </ConversationContent>
                <ConversationScrollButton/>
            </Conversation>

            <PromptInput onSubmit={sendMessage} className="max-w-4/5 m-auto mt-4 relative">
                <PromptInputTextarea
                    value={prompt}
                    onChange={(e) => {
                        setPrompt(e.target.value)
                    }}
                />
                <PromptInputToolbar>
                    <PromptInputSubmit
                        className="absolute right-1 bottom-1"
                        disabled={false}
                        status={'ready'}
                    />
                </PromptInputToolbar>
            </PromptInput>
        </div>
    )
};

import {
    PromptInput,
    PromptInputSubmit,
    PromptInputTextarea,
    PromptInputToolbar
} from "@/components/ai-elements/prompt-input";
import React, {FormEvent, useState} from "react";

interface MessageInputProps {
    onSubmit: (message: string) => void;
    isLoading: boolean;
}

export default function MessageInput({ onSubmit, isLoading }: MessageInputProps) {
    const [prompt, setPrompt] = useState("");

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        if (!prompt.trim()) return;

        onSubmit(prompt);
        setPrompt("");
    };

    return (
        <PromptInput onSubmit={handleSubmit} className="max-w-4/5 m-auto mt-4 relative">
            <PromptInputTextarea
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
            />
            <PromptInputToolbar>
                <PromptInputSubmit
                    className="absolute right-1 bottom-1"
                    disabled={isLoading}
                    status={isLoading ? 'submitted' : 'ready'}
                />
            </PromptInputToolbar>
        </PromptInput>
    );
}